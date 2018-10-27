    # to create project from scratch
    sam init --runtime java8

**To deploy**

    # to create cfn script and upload src using SAM
    sam package \
        --region eu-central-1 \
        --template-file template.yaml \
        --output-template-file serverless-output.yaml \
        --s3-bucket meetup-lambda-src-test

<br/>

    # to deploy cfn using SAM
    sam deploy \
        --region eu-central-1 \
        --template-file serverless-output.yaml \
        --stack-name "meetup-lambda-sam" \
        --capabilities CAPABILITY_NAMED_IAM

<br/>

    # to deploy cfn using AWS Cloudformation
    aws cloudformation deploy \
        --template-file serverless-output.yaml \
        --stack-name "meetup-lambda-sam" \
        --capabilities CAPABILITY_NAMED_IAM

<br/>

    # to generate s3 event
    sam local generate-event s3 \
        --bucket meetup-lambda-sam-test \
        --key 562810932035_us-east-1_elb.log > s3event.json

<br/>

    # to run function locally without deploying
    sam local invoke -e s3event.json -t template.yaml LambdaFunction

        
**To undeploy**

    # to clean up S3 bucket
    aws s3 rm s3://meetup-lambda-sam-test --recursive

<br/>

    # to delete cloudformation stack
    aws cloudformation delete-stack \
        --region eu-central-1 \
        --stack-name "meetup-lambda-sam"