# Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
#
# This file is licensed under the Apache License, Version 2.0 (the "License").
# You may not use this file except in compliance with the License. A copy of
# the License is located at
#
# http://aws.amazon.com/apache2.0/
#
# This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
# CONDITIONS OF ANY KIND, either express or implied. See the License for the
# specific language governing permissions and limitations under the License.
#
#


import boto3


def upload(source_file, bucket_name, object_key):
    s3 = boto3.resource('s3')

    # Uploads the source file to the specified s3 bucket by using a
    # managed uploader. The uploader automatically splits large
    # files and uploads parts in parallel for faster uploads.
    try:
        s3.Bucket(bucket_name).upload_file(source_file, object_key)
    except Exception as e:
        print(e)


def main():
    import argparse

    parser = argparse.ArgumentParser()
    parser.add_argument('source_file', help='The path and name of the source file to upload.')
    parser.add_argument('bucket_name', help='The name of the destination bucket.')
    parser.add_argument('object_key', help='The key of the destination object.')
    args = parser.parse_args()

    upload(args.source_file, args.bucket_name, args.object_key)


if __name__ == '__main__':
    main()


