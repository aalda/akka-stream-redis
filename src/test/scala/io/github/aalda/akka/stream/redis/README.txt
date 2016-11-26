All these tests require a locally running Redis server that does not require any authorization.

When using docker, a temporary server can by started by running:

```
docker run --rm -p 6379:6379 redis:3.0.7
```