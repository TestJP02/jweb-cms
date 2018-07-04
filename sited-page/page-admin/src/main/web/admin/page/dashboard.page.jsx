import MostVisitedReport from "./dashboard.page.most-visited";

window.app.bundle("dashboardBundle").addDashboard({
    name: "Daily Most Visited",
    messageKey: "page.mostMostVisitedReport",
    component: MostVisitedReport
});