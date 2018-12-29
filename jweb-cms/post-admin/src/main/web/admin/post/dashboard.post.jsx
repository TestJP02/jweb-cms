import MostVisitedReport from "./dashboard.post.most-visited";

window.app.bundle("dashboardBundle").addDashboard({
    name: "Daily Most Visited",
    messageKey: "post.mostMostVisitedReport",
    component: MostVisitedReport
});