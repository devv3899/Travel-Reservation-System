
mvn clean package

aws cloudformation package --template-file sam.yaml --output-template-file output-sam.yaml --s3-bucket devflights


aws cloudformation deploy --template-file output-sam.yaml --stack-name FlightsJerseyApi --capabilities CAPABILITY_IAM


aws cloudformation describe-stacks --stack-name FlightsJerseyApi --query 'Stacks[0].Outputs[*].{Service:OutputKey,Endpoint:OutputValue}'

