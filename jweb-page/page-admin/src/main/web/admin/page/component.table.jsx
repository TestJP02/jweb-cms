import React from "react";
import PropTypes from "prop-types";
import {Input} from "element-react";

import "./component.table.css";

export default class TableComponent extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            mode: props.mode,
            component: props.component,
            created: false,
            maxRow: 8,
            maxCol: 8,
            previewCreateRow: 0,
            previewCreateCol: 0
        };
    }

    componentWillMount() {
        if (this.state.component.attributes && this.state.component.attributes.data) {
            this.setState({created: true});
        }
    }

    componentWillReceiveProps(props) {
        this.setState({
            mode: props.mode,
            component: props.component
        });
    }

    createTable(row, col) {
        const component = this.state.component;
        component.attributes.data = [];
        for (let i = 0; i <= row; i += 1) {
            const rowData = [];
            for (let j = 0; j <= col; j += 1) {
                rowData.push(null);
            }
            component.attributes.data.push(rowData);
        }
        this.setState({
            component,
            created: true
        });
        this.props.onChange(component);
    }

    changeData(value, row, col) {
        const component = this.state.component;
        component.attributes.data[row][col] = value;
        this.setState({component});
    }

    render() {
        if (!this.state.created) {
            return <div className="page-table-add">
                {Array(this.state.maxRow).fill().map((row, i) =>
                    <div key={i}>
                        {Array(this.state.maxCol).fill().map((col, j) =>
                            <div key={j}
                                className="page-table-add__cell"
                                style={{
                                    "border-color": i <= this.state.previewCreateRow && j <= this.state.previewCreateCol ? "#3498db" : "#ddd",
                                    "background-color": i <= this.state.previewCreateRow && j <= this.state.previewCreateCol ? "rgba(52, 152, 219,0.6)" : "#fff"
                                }}
                                onMouseEnter={() => this.setState({
                                    previewCreateRow: i,
                                    previewCreateCol: j
                                })}
                                onClick={() => this.createTable(i, j)}>
                            </div>
                        )}
                    </div>
                )}
            </div>;
        }
        return <div className="page-table">
            <table className="page-table__table">
                <thead>
                    <tr>
                        {this.state.component.attributes.data[0].map((data, col) =>
                            <td key={col}>
                                <Input type="textarea" autosize={true} value={data}
                                    onChange={value => this.changeData(value, 0, col)}
                                    onBlur={() => this.props.onChange(this.state.component)} />
                            </td>
                        )}
                    </tr>
                </thead>
                <tbody>
                    {this.state.component.attributes.data.map((rowData, row) =>
                        row > 0 && <tr key={row}>{rowData.map((colData, col) =>
                            <td key={col} >
                                <Input type="textarea" autosize={true} value={colData}
                                    onChange={value => this.changeData(value, row, col)}
                                    onBlur={() => this.props.onChange(this.state.component)} />
                            </td>
                        )}</tr>
                    )}
                </tbody>
            </table>
        </div>;
    }
}

TableComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};