import TotalPagesReport from "./dashboard.page.total-pages";

window.app.bundle("dashboardBundle").addDashboard({
    name: "Total Pages",
    messageKey: "page.totalPages",
    component: TotalPagesReport
});