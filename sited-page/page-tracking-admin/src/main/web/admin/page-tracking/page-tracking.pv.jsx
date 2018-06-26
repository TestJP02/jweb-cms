import React from "react";
import {CartesianGrid, Legend, Line, LineChart, Tooltip, XAxis, YAxis} from "recharts";

export default class PVReport extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            query: {},
            data: []
        };
    }

    componentWillMount() {
        fetch("/admin/api/page/tracking/visit", {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response});
        });
    }

    render() {
        return (
            <LineChart width={600} height={300} data={this.state.data}
                margin={{
                    top: 5,
                    right: 30,
                    left: 20,
                    bottom: 5
                }}>
                <XAxis dataKey="timestamp"/>
                <YAxis/>
                <CartesianGrid strokeDasharray="3 3"/>
                <Tooltip/>
                <Legend/>
                <Line type="monotone" dataKey="total" stroke="#8884d8" activeDot={{r: 8}}/>
            </LineChart>
        );
    }
}