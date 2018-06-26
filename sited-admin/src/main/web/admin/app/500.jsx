import React from "react";
import {Button} from "element-react";
import {Link} from "react-router-dom";

export default function Error500() {
    return (
        <div className="page sited-error__page">
            <div className="sited-error__container">
                <img src="/admin/app/static/500.png" alt="" className="sited-error__image"/>
                <p className="sited-error__text">There is no data on this page！！！</p>
                <Button type="primary" className="sited-error__button">
                    <Link to={{pathname: "/admin/"}}> Index </Link>
                </Button>
            </div>
        </div>
    );
}