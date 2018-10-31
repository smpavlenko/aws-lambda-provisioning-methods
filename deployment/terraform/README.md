
**To deploy**

    # to initialize a Terraform working directory
    terraform init

<br/>

    # to see the incoming changes in infrastructure
    terraform plan

<br/>

    # to build or change infrastructure
    terraform apply -auto-approve

**To undeploy**

    # to clean up S3 bucket
    aws s3 rm s3://meetup-lambda-tf-test --recursive

<br/>

    # to destroy infrastructure
    terraform destroy