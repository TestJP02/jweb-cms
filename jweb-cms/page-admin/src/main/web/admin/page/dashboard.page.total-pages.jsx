import React from "react";

const i18n = window.i18n;

export default class TotalPagesReport extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            query: {
                status: "ACTIVE",
                page: 1,
                limit: 99999
            },
            data: 0
        };
    }

    componentWillMount() {
        fetch("/admin/api/page/find", {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response.total});
        });
    }

    render() {
        return (
            <div>{i18n.t("page.total")}:{this.state.data}</div>
        );
    }
}