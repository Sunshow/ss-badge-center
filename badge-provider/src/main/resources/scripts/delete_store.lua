local storeKey = KEYS[1]
local manageKey = KEYS[2]
local storeName = ARGV[1]

local hasStore = redis.call('exists', storeKey)
if (hasStore == 0)
then
    return -1
end

-- 1. delete store
redis.call('del', storeKey)

-- 2. delete all node
for i = 3, #KEYS do
    redis.call('del', KEYS[i])
end

-- 3. remove from store set
redis.call('srem', manageKey, storeName)

return 1