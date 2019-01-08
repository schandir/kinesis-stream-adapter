# kinesis-stream-adapter

This is spring cloud based aws kinesis stream consumer. This spring cloud based wrapper for the kinesis stream is multi-threaded and handles iterating over shards to consume data from the streams. A gradle version of the project is also available but is not checked in bitbucket

#Instructions to build and run
1. Clone the project to local
2. Run './gradlew clean build' from command line
4. Go to resources folder and update the aws credentials
5. Go to target folder and run java -jar kinesis-stream-adapter*.jar (minimum jdk 8 version is required)
