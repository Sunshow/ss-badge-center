local storeKey = KEYS[1]
local nodeKey = KEYS[2]

-- 1. get path set count (resource count)
local resourceCount = redis.call('scard', nodeKey)

-- 2. remove the whole node
local affected = redis.call('del', nodeKey)
if (affected == 0)
then
    return
end

if (resourceCount == 0)
then
    return
end

-- 3. maintain current path and ancestor nodes count
local hmget = {}
hmget[1] = 'hmget'
hmget[2] = storeKey
for i = 1, #ARGV do
    hmget[#hmget + 1] = ARGV[i]
end
local hmgetResult = redis.call(unpack(hmget))

local hmset = {}
hmset[1] = 'hmset'
hmset[2] = storeKey
for i = 1, #ARGV do
    hmset[#hmset + 1] = ARGV[i]
    hmset[#hmset + 1] = tostring(tonumber(hmgetResult[i]) - resourceCount)
end
redis.call(unpack(hmset))

return