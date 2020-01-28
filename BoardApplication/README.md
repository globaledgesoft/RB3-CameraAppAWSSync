# AWS SDK for Python (Boto 3) application


This is an application for the AWS SDK for Python (Boto 3) to upload images onto AWS S3 storage from RB3 board.

## Prerequisites


To build and run the Python script, you'll need:

- [Python 3 ](https://www.python.org/downloads/)
- [pip ](https://pip.pypa.io/en/stable/installing/)
- [AWS SDK for Python (Boto 3)](https://boto3.amazonaws.com/v1/documentation/api/latest/guide/quickstart.html)
- AWS Account

For instructions on installing the AWS SDK for Python (Boto 3) and setting up
credentials, see `Boto 3 Docs Quickstart <https://boto3.amazonaws.com/v1/documentation/api/latest/guide/quickstart.html>`_.

## Running the application


The application is written for execution in a command prompt window or
Python console.

Create a credentials script file
- To easily set the environment, default region (say file eg. env.sh) copy the keys and region as shown below
	export AWS_ACCESS_KEY_ID=”Your Access key”
	export AWS_SECRET_ACCESS_KEY=”Your Secret key”
	export AWS_DEFAULT_REGION=”Your default region”
- Run the env.sh once on the board to set your generated keys and region of your choice. Now run the upload.py script as given below.

To run the upload.py example in a command prompt window, import Boto3 and make requests and process responses from the AWS S3 service as shown in upload.py file. For eg. run the file as shown below

::

    python -m upload bucket_name region

## Documentation


For detailed documentation for the AWS SDK for Python (Boto 3), kindly go through the links below:

- [AWS SDK for Python (Boto 3) Documentation](https://docs.aws.amazon.com/pythonsdk/)

- [Boto 3 Docs Quickstart](https://boto3.amazonaws.com/v1/documentation/api/latest/guide/quickstart.html)

