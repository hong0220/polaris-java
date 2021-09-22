熔断：
curl -L -X GET 'localhost:48080/example/service/a/getBServiceInfo'
trigger the refuse for service b

正常：
curl -L -X GET 'localhost:48081/example/service/b/getAServiceInfo'
hello world ! I'am a service