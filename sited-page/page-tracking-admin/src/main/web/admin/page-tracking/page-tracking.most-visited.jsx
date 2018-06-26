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
                    label: i18n.t("page-tracking.title"),
                    prop: "title",
                    render: data => <a href={data.path} target="_blank">{data.title}</a>
                },
                {
                    label: i18n.t("page-tracking.total"),
                    prop: "totalVisited"
                },
                {
                    label: i18n.t("page-tracking.timestamp"),
                    prop: "timestamp"
                }
            ]
        };
    }

    componentWillMount() {
        fetch("/admin/api/page/tracking/most-visit", {
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