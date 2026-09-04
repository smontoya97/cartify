#!/bin/sh
set -e

ENDPOINT="http://dynamodb-local:8000"
REGION="us-east-1"

echo "Waiting for DynamoDB Local to be ready..."
until aws dynamodb list-tables --endpoint-url "$ENDPOINT" --region "$REGION" > /dev/null 2>&1; do
  sleep 2
done
echo "DynamoDB Local is up."

create_table_if_missing() {
  TABLE_NAME=$1
  shift
  if aws dynamodb describe-table --table-name "$TABLE_NAME" --endpoint-url "$ENDPOINT" --region "$REGION" > /dev/null 2>&1; then
    echo "Table '$TABLE_NAME' already exists, skipping."
  else
    echo "Creating table '$TABLE_NAME'..."
    aws dynamodb create-table --table-name "$TABLE_NAME" --endpoint-url "$ENDPOINT" --region "$REGION" "$@" > /dev/null
    echo "Table '$TABLE_NAME' created."
  fi
}

# customers
create_table_if_missing customers \
  --attribute-definitions AttributeName=pk,AttributeType=S AttributeName=sk,AttributeType=S \
  --key-schema AttributeName=pk,KeyType=HASH AttributeName=sk,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST

# products: GSI1 for ListProductsByStatus
create_table_if_missing products \
  --attribute-definitions \
      AttributeName=pk,AttributeType=S AttributeName=sk,AttributeType=S \
      AttributeName=gsi1pk,AttributeType=S AttributeName=gsi1sk,AttributeType=S \
  --key-schema AttributeName=pk,KeyType=HASH AttributeName=sk,KeyType=RANGE \
  --global-secondary-indexes \
      '[{"IndexName":"GSI1","KeySchema":[{"AttributeName":"gsi1pk","KeyType":"HASH"},{"AttributeName":"gsi1sk","KeyType":"RANGE"}],"Projection":{"ProjectionType":"ALL"}}]' \
  --billing-mode PAY_PER_REQUEST

# orders: GSI1 (by customer) and GSI2 (by status, month-sharded)
create_table_if_missing orders \
  --attribute-definitions \
      AttributeName=pk,AttributeType=S AttributeName=sk,AttributeType=S \
      AttributeName=gsi1pk,AttributeType=S AttributeName=gsi1sk,AttributeType=S \
      AttributeName=gsi2pk,AttributeType=S AttributeName=gsi2sk,AttributeType=S \
  --key-schema AttributeName=pk,KeyType=HASH AttributeName=sk,KeyType=RANGE \
  --global-secondary-indexes \
      '[{"IndexName":"GSI1","KeySchema":[{"AttributeName":"gsi1pk","KeyType":"HASH"},{"AttributeName":"gsi1sk","KeyType":"RANGE"}],"Projection":{"ProjectionType":"ALL"}},{"IndexName":"GSI2","KeySchema":[{"AttributeName":"gsi2pk","KeyType":"HASH"},{"AttributeName":"gsi2sk","KeyType":"RANGE"}],"Projection":{"ProjectionType":"ALL"}}]' \
  --billing-mode PAY_PER_REQUEST

echo "All tables are ready."