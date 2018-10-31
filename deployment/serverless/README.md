#Serverless Framework

    # to create project from scratch
    sls create --template aws-java8 --path my-service

**To deploy**

    # to deploy new and update current
    sls deploy --stage dev
        
**To undeploy**

    # to clean up S3 bucket
    aws s3 rm s3://meetup-lambda-sls-test --recursive

<br/>

    # to remove infrastructure
    sls remove --stage dev