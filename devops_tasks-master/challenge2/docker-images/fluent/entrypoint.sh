#!/usr/bin/dumb-init /bin/sh

uid=${FLUENT_UID:-1000}

# check if a old fluent user exists and delete it
cat /etc/passwd | grep fluent
if [ $? -eq 0 ]; then
    deluser fluent
fi

# (re)add the fluent user with $FLUENT_UID
adduser -D -g '' -u ${uid} -h /home/fluent fluent

# chown home and data folder
chown -R fluent /home/fluent
chown -R fluent /fluentd
mkdir -p /etc/td-agent
chown -R fluent /etc/td-agent
fluentd -c /fluentd/etc/${FLUENTD_CONF} -p /fluentd/plugins $FLUENTD_OPT -vv --dry-run
if [ $? -ne 0 ]
then
echo "Fluentd Configuration Problem"
echo "Could not Start Log Sidecar"
sleep 5m
exit 1
fi
exec su-exec fluent "$@"
