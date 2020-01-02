from lib.mmonit import MmonitBaseAction


class MmonitListUptimeHosts(MmonitBaseAction):
    def run(self, uptime_range=0, datefrom=0, dateto=0):
        self.login()

        if datefrom != 0 and uptime_range != 12:
            raise Exception("If datefrom is set, range should be 12")
        data = {"range": uptime_range, "datefrom": datefrom, "dateto": dateto}
        req = self.session.post("{}/reports/uptime/list".format(self.url), data=data)

        try:
            return req.json()
        except Exception:
            raise
        finally:
            self.logout()
