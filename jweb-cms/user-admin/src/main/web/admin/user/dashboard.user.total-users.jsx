import React from "react";

const i18n = window.i18n;

export default class TotalUsersReport extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            query: {},
            data: 0
        };
    }

    componentWillMount() {
        fetch("/admin/api/user/total", {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response});
        });
    }

    render() {
        return (
            <div>
                {i18n.t("user.total")}:{this.state.data}
            </div>
        );
    }
}