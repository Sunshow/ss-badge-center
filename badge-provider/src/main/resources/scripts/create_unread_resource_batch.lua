local storeKey = KEYS[1]
local nodeKey = KEYS[2]
local pathCount = tonumber(ARGV[1])

local hasStore = redis.call('exists', storeKey)
if (hasStore == 0)
then
    return -1
end

-- 1. add all resources to path set
local resources = {}
for i = pathCount + 2, #ARGV do
    resources[#resources + 1] = ARGV[i]
end
local resourceAdd = redis.call('sadd', nodeKey, unpack(resources))
if (resourceAdd == 0)
then
    return 0
end

-- 2. get path set count (resource count)
local resourceCount = redis.call('scard', nodeKey)
-- 3. update new count to store
redis.call('hset', storeKey, ARGV[2], tostring(resourceCount))
-- 4. maintain ancestor nodes count
local hmget = {}
hmget[1] = 'hmget'
hmget[2] = storeKey
for i = 3, pathCount + 1 do
    hmget[#hmget + 1] = ARGV[i]
end
local hmgetResult = redis.call(unpack(hmget))

local hmset = {}
hmset[1] = 'hmset'
hmset[2] = storeKey
for i = 3, pathCount + 1 do
    hmset[#hmset + 1] = ARGV[i]
    if (hmgetResult[i - 2] == nil or (type(hmgetResult[i - 2]) == 'boolean' and not hmgetResult[i - 2]))
    then
        hmset[#hmset + 1] = tostring(resourceAdd)
    else
        hmset[#hmset + 1] = tostring(tonumber(hmgetResult[i - 2]) + resourceAdd)
    end
end
redis.call(unpack(hmset))

return 1