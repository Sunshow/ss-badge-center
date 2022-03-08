local key1 = KEYS[1]
local key2 = KEYS[2]

redis.call('incr', key1)
redis.call('expire', key1, 10)

local cycles
for i = 0, ARGV[1], 1 do
    local keyExists = redis.call("EXISTS", key1)
    cycles = i;
    if keyExists == 0 then
        break ;
    end
end

redis.call('incr', key2)

return redis.call('mget', key1, key2)