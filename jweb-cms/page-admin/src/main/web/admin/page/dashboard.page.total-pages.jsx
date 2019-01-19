import React from "react";

const i18n = window.i18n;

export default class TotalPagesReport extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            statistics: {
                total: 0,
                pages: []
            }
        };
    }

    componentWillMount() {
        fetch("/admin/api/page/statistics", {
            method: "GET",
            headers: {"Content-Type": "application/json"}
        }).then((response) => {
            this.setState({statistics: response});
        });
    }

    render() {
        return (
            <div>
                <div><h1>{i18n.t("page.total")}:{this.state.statistics.total}</h1></div>
                <Recharts.PieChart width={300} height={300}>
                    <Recharts.Pie isAnimationActive={true} nameKey="status" dataKey="total" data={this.state.statistics.pages} cx={150} cy={150} outerRadius={80} fill="#8884d8" label/>
                    <Recharts.Tooltip/>
                </Recharts.PieChart>
            </div>
        );
    }
}