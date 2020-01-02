# WebPageTest Pack

Interfaces with the public WebPageTest.org instance, or your own private instance.

Requires:
 * `requests==2.6.0`
 * API Key from [WebPageTest.org](http://www.webpagetest.org/getkey.php) (Only for the public instance.)

## Configuration

Copy the example configuration in [webpagetest.yaml.example](./webpagetest.yaml.example)
to `/opt/stackstorm/configs/webpagetest.yaml` and edit as required.

It must contain:

 * `wpt_url` - Base URL for the WebPageTest instance to use. Defaults to `http://webpagetest.org`.
 * `key` - The API key you received from the [WebPageTest Public Instance](http://www.webpagetest.org/getkey.php). Private instances of WebPageTest do not require a key.

You can also use dynamic values from the datastore. See the
[docs](https://docs.stackstorm.com/reference/pack_configs.html) for more info.

## Actions

 * `list_locations` - Lists the available testing locations for your instance. Takes no arguments.
 * `request_test` - Requests a test to be run. You must specify the `domain` to be tested, as well as the `location` from which to test. The location must be formatted exactly how the `list_locations` action returns the list.
 * `random_test` - Requests a test to run from a randomly selected location. You must specify the `domain` to be tested.
