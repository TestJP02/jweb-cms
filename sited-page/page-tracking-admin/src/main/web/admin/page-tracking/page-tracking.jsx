import PVReport from "./page-tracking.pv";
import MostVisitedReport from "./page-tracking.most-visited";

window.app.bundle("dashboardBundle").addDashboard({
    name: "PV report",
    messageKey: "page-tracking.pvReport",
    component: PVReport
});

window.app.bundle("dashboardBundle").addDashboard({
    name: "Page Rank",
    messageKey: "page-tracking.mostVisitedReport",
    component: MostVisitedReport
});