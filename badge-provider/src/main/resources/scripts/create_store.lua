local storeKey = KEYS[1]
local manageKey = KEYS[2]
local storeName = ARGV[1]

-- 1. add to store manage
local affected = redis.call('sadd', manageKey, storeName)
if (affected == 0)
then
    return 0
end

-- 2. init store
redis.call('hset', storeKey, '/', '0')

return 1