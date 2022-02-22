local storeKey = KEYS[1]
local storeVal = ARGV[1]

redis.call('set', storeKey, storeVal)

return redis.call('get', storeKey)