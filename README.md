# ss-badge-center

A badge management center.

一个基于树形层级的角标（小红点）管理系统。

基于 Redis 实现。

## API

### Create Store

```bash
curl -X PUT -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/ -d '{"name": "sample"}'
```

### List Store

```bash
curl -X GET -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/
```

### Delete Store

```bash
curl -X DELETE -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample
```

### Create Unread

```bash
curl -X PUT -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/foo/bar -d '{"resource": "a_resource_key"}'

curl -X PUT -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/foo/bar -d '{"resources": ["key1", "key2", "key3"]}'
```

### Get Unread Badge

```bash
curl -X GET -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/foo/bar

curl -X GET -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/foo

# 注意末尾的斜杠, 表示路径顶层节点
curl -X GET -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/

# ResponseText: {"count":0}
```

### Batch Get Unread Badges

```bash
curl -X GET -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample?paths=/,/foo,/foo/bar

# ResponseText: {"count":{"/":3,"/foo":3,"/foo/bar":3}}
```

### Delete Unread

```bash
curl -X DELETE -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/foo/bar -d '{"resource": "a_resource_key"}'
```

### Delete All

```bash
curl -X DELETE -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/foo/bar
```