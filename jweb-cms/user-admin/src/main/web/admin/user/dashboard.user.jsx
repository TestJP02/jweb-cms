import TotalUsersReport from "./dashboard.user.total-users";

window.app.bundle("dashboardBundle").addDashboard({
    name: "Total Users",
    messageKey: "user.totalUserReport",
    component: TotalUsersReport
});