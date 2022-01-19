# ss-badge-center

A badge management center.

一个基于树形层级的角标（小红点）管理系统。

## API

### Create Store

```bash
curl -X PUT -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/ -d '{"name": "sample"}'
```

### Get Store

```bash
# 注意末尾没有斜杠
curl -X GET -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample
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
```

### Get Unread Badge

```bash
curl -X GET -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/foo/bar

curl -X GET -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/foo

# 注意末尾的斜杠, 表示路径顶层节点
curl -X GET -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/
```

### Delete Unread

```bash
curl -X DELETE -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/foo/bar -d '{"resource": "a_resource_key"}'
```

### Delete All

```bash
curl -X DELETE -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample/foo/bar
```