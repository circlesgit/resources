#!/bin/bash
set -eo pipefail

dir="$(dirname "$(readlink -f "$BASH_SOURCE")")"

serverImage="$1"

# Use a client image with curl for testing
clientImage='buildpack-deps:stretch-curl'

# Create an instance of the container-under-test
cid="$(docker run -d "$serverImage")"
trap "docker rm -vf $cid > /dev/null" EXIT

_request() {
	local method="$1"
	shift

	local url="${1}"
	shift

	docker run --rm --link "$cid":ghost "$clientImage" \
		curl -fs -X"$method" "$@" "http://ghost:2368/$url"
}

# Make sure that Ghost is listening and ready
. "$dir/../../retry.sh" '_request GET / --output /dev/null'

# Check that /ghost/ redirects to setup (the image is unconfigured by default)
ghostVersion="$(docker inspect --format '{{range .Config.Env}}{{ . }}{{"\n"}}{{end}}' "$serverImage" | awk -F= '$1 == "GHOST_VERSION" { print $2 }')"
case "$ghostVersion" in
	0.*) _request GET '/ghost/' -I | grep -q '^Location: .*setup' ;;
	*)   _request GET '/ghost/api/v0.1/authentication/setup/' | grep -q 'status":false' ;;
esac

