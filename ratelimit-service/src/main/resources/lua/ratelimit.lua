redis.replicate_commands()

local listLen,time
listLen = redis.call('LLEN', KEYS[1])

if listLen and tonumber(listLen) < tonumber(ARGV[1]) then
    local a = redis.call('TIME');
    redis.call('LPUSH', KEYS[1], a[1]*1000000+a[2])
else
    time = redis.call('LINDEX', KEYS[1], -1)
    local a = redis.call('TIME');
    if a[1]*1000000+a[2] - time < tonumber(ARGV[2])*1000000 then
        return 0;
    else
        redis.call('LPUSH', KEYS[1], a[1]*1000000+a[2])
        redis.call('LTRIM', KEYS[1], 0, tonumber(ARGV[1])-1)
    end
end

return 1;