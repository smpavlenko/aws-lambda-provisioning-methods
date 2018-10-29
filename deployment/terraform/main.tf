variable aws_region {
  description = "The AWS region to create things in."
  default = "eu-central-1"
}

variable aws_function_name {
  default = "meetup-lambda-tf"
}

variable aws_s3_bucket_name {
  default = "meetup-lambda-tf-test"
}

variable aws_lambda_role_name {
  type = "string"
  default = "meetup-lambda-tf-role"
}

variable aws_tag_env {
  default = "Test"
}

variable src_file_path {
  type = "string"
  default = "../../app/target/app-1.0-SNAPSHOT.jar"
}


provider "aws" {
  region = "${var.aws_region}"
}

data "aws_iam_policy_document" "policy" {
  statement {
    sid = ""
    effect = "Allow"

    principals {
      identifiers = ["lambda.amazonaws.com"]
      type = "Service"
    }

    actions = ["sts:AssumeRole"]
  }
}

resource "aws_s3_bucket" "s3bucket" {
  bucket = "${var.aws_s3_bucket_name}"

  tags {
    Env = "${var.aws_tag_env}"
  }
}

resource "aws_iam_role" "iam_for_lambda" {
  name = "${var.aws_lambda_role_name}"
  assume_role_policy = "${data.aws_iam_policy_document.policy.json}"

}

resource "aws_iam_role_policy" "lambdaS3Policy" {
  name = "LambdaS3Policy"
  role = "${aws_iam_role.iam_for_lambda.id}"
  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "s3:GetObject",
        "s3:PutObject"
        ],
      "Resource": [
        "arn:aws:s3:::${aws_s3_bucket.s3bucket.id}",
        "arn:aws:s3:::${aws_s3_bucket.s3bucket.id}/*"
        ],
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
POLICY
}

resource "aws_iam_role_policy" "lambdaCWPolicy" {
  name = "LambdaCWPolicy"
  role = "${aws_iam_role.iam_for_lambda.id}"
  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
        ],
      "Resource": [ "*" ],
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
POLICY
}

resource "aws_lambda_permission" "allow_bucket" {
  statement_id  = "AllowExecutionFromS3Bucket"
  action        = "lambda:InvokeFunction"
  function_name = "${aws_lambda_function.lambda.arn}"
  principal     = "s3.amazonaws.com"
  source_arn    = "${aws_s3_bucket.s3bucket.arn}"
}

resource "aws_lambda_function" "lambda" {
  function_name = "${var.aws_function_name}"

  filename = "${var.src_file_path}"
  source_code_hash = "${base64sha256(file("${var.src_file_path}"))}"

  role = "${aws_iam_role.iam_for_lambda.arn}"
  handler = "com.pavlenko.lambda.LambdaFunction::handleRequest"
  runtime = "java8"
  memory_size = 320
  timeout = 150
  tags {
    Env = "${var.aws_tag_env}"
  }
}

resource "aws_s3_bucket_notification" "bucket_notification" {
  bucket = "${aws_s3_bucket.s3bucket.id}"

  lambda_function {
    lambda_function_arn = "${aws_lambda_function.lambda.arn}"
    events = ["s3:ObjectCreated:*"]
    filter_prefix = "input"
  }
}
