# Licensed to the StackStorm, Inc ('StackStorm') under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import requests
from lib.base import API_HOST


def GetScanList(config, customer_id=None, active_only=False):
    """
    The template class for

    Returns: An blank Dict.

    Raises:
    ValueError: On lack of key in config.
    """

    results = {}

    url = "https://{}/api/scan/v1/scans".format(API_HOST)
    payload = None
    headers = {"Accept": "application/json"}

    if customer_id is not None:
        payload = {}
        payload['customer_id'] = customer_id

    try:
        r = requests.get(url,
                         headers=headers,
                         auth=(config['api_key'], ''),
                         params=payload)
        r.raise_for_status()
    except:
        raise ValueError("HTTP error: %s on %s" % (r.status_code, r.url))

    try:
        data = r.json()
    except:
        raise ValueError("Invalid JSON")
    else:
        for item in data:
            if item["active"] is False and active_only is True:
                continue

            results[item['title']] = {"active": item["active"],
                                      "id": item["id"],
                                      "type": item["type"]}
    return results
