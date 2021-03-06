import "whatwg-fetch";

window.$fetch = fetch;
window.$fetchInterceptors = [];
window.fetch = function(url, options) {
    function parseJSON(response) {
        return new Promise(resolve => response.text().then(text => resolve({
            status: response.status,
            ok: response.ok,
            json: text ? JSON.parse(text) : {}
        })));
    }

    return new Promise((resolve, reject) => {
        const headers = Object.assign({}, {
            "Accept": "application/json",
            "Content-Type": "application/json"
        }, options && options.headers ? options.headers : {});
        const defaultOptions = {credentials: "include"};
        const finalOption = Object.assign({}, defaultOptions, options, {headers});

        window.$fetch(url, finalOption).then(parseJSON, e => reject(e)).then((response) => {
            for (let i = 0; i < window.$fetchInterceptors.length; i += 1) {
                if (!window.$fetchInterceptors[i](response)) {
                    break;
                }
            }
            if (response.ok) {
                return resolve(response.json);
            }
            return reject(response.json);
        });
    });
};
window.fetchIntercept = (interceptor) => {
    window.$fetchInterceptors.push(interceptor);
};
