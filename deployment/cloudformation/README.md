**To deploy**

    # to create cloudformation stack
    aws cloudformation create-stack \
        --region eu-central-1 \
        --stack-name "meetup-lambda-cfn" \
        --template-body file://lambda_cfn.yaml \
        --parameters file://lambda_params.json \
        --tags Key=Env,Value=Test \
        --capabilities CAPABILITY_NAMED_IAM

<br/>

    # to update cloudformation stack
    aws cloudformation update-stack \
        --region eu-central-1 \
        --stack-name "meetup-lambda-cfn" \
        --template-body file://lambda_cfn.yaml \
        --parameters file://lambda_params.json \
        --tags Key=Env,Value=Test \
        --capabilities CAPABILITY_NAMED_IAM