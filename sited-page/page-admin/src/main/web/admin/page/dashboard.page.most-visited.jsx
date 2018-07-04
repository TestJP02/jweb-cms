import React from "react";
import {Table} from "element-react";

const i18n = window.i18n;

export default class MostVisitedReport extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            query: {},
            data: [],
            columns: [
                {
                    label: i18n.t("page.title"),
                    prop: "title",
                    render: data => <a href={data.path} target="_blank">{data.title}</a>
                },
                {
                    label: i18n.t("page.totalVisited"),
                    prop: "totalVisited"
                },
                {
                    label: i18n.t("page.totalDailyVisited"),
                    prop: "totalDailyVisited"
                },
                {
                    label: i18n.t("page.totalWeeklyVisited"),
                    prop: "totalWeeklyVisited"
                },
                {
                    label: i18n.t("page.totalMonthlyVisited"),
                    prop: "totalMonthlyVisited"
                }
            ]
        };
    }

    componentWillMount() {
        fetch("/admin/api/page/statistics/most-visited", {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response});
        });
    }

    render() {
        return (
            <Table
                stripe={true}
                style={{width: "100%"}}
                columns={this.state.columns}
                data={this.state.data.items}
            />
        );
    }
}