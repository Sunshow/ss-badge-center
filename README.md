# ss-badge-center
A badge management center.

一个基于树形层级的角标（小红点）管理系统。

## API

### Badge Store

```bash
curl -X PUT -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/ -d '{"name": "sample"}'
```

```bash
curl -X DELETE -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample
```

```bash
curl -X PUT -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample -d '{"path": "/foo/bar", "resource": "a_resource_key"}'
```

```bash
curl -X POST -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample -d '{"path": "/foo/bar", "resource": "a_resource_key"}'
```

```bash
curl -X DELETE -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/sample -d '{"path": "/foo/bar", "resource": "a_resource_key"}'
```