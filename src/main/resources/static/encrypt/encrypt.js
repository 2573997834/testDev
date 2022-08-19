document.write('<script src="/static/encrypt/jsencrypt.min.js" type="text/javascript" charset="utf-8"></script>');
document.write('<script src="/static/jquery/jquery.min.js" type="text/javascript" charset="utf-8"></script>');
var PUBLIC_KEY = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAOHCjmYLWCqESh4phsGZiQ3ptyrmP/8hJe7ZFOx1lrQnOGSB69zW1AcABEKU34J9hvq+JA/3rSGT7u8PvCAS5R8CAwEAAQ==';
var PRIVATE_KEY = 'MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEA4cKOZgtYKoRKHimGwZmJDem3KuY//yEl7tkU7HWWtCc4ZIHr3NbUBwAEQpTfgn2G+r4kD/etIZPu7w+8\n' +
    'IBLlHwIDAQABAkA6XySUa+B69cN4MwJ9siYGq+RJOklXvQlizUwkq26w22rPWN/8K3zpiQoLV8zb/q2ipGHQsI1FlOINxtbDRdCRAiEA+AOypmy9H90jBOvRH0kvUI4c\n' +
    'qd/UoiNKiXUdR8wqAtkCIQDpB2wRraB+Ub+S73Xl2FntOQHq/wQl9WLMRjhN5GA8twIhALcDu4wVx8XAoDvcbFfi4HhYNgyg8D6pPjKK6o11ujaZAiB/vO7Tnf7NX9iJ\n' +
    'HjTdosRg0pAnllVazXG0EoYIxLiwbwIhAPZDsiSZiux7/1eba8j3sZUyCgtnJYrpmgA5nB6vsSUn';

function encrypt(data){
    let encrypt = new JSEncrypt();
    encrypt.setPublicKey(PUBLIC_KEY);
    let ciphertext = encrypt.encrypt(data);
    return ciphertext;
}

function decrypt(data){
    let encrypt = new JSEncrypt();
    encrypt.setPublicKey(PRIVATE_KEY);
    let text = encrypt.decrypt(data);
    return text;
}

