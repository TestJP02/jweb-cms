import React from "react";

const i18n = window.i18n;

export default class TotalUsersReport extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            statistics: {
                total: 0,
                channels: []
            }
        };
    }

    componentWillMount() {
        fetch("/admin/api/user/statistics", {
            method: "GET",
            headers: {"Content-Type": "application/json"}
        }).then((response) => {
            this.setState({statistics: response});
        });
    }

    render() {
        return (
            <div>
                <div><h1>{i18n.t("user.total")}:{this.state.statistics.total}</h1></div>
                <Recharts.PieChart width={300} height={300}>
                    <Recharts.Pie isAnimationActive={true} nameKey="channel" dataKey="total" data={this.state.statistics.channels} cx={150} cy={150} outerRadius={80} fill="#8884d8" label/>
                    <Recharts.Tooltip/>
                </Recharts.PieChart>
            </div>
        );
    }
}