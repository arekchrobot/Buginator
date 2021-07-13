import React, { useState } from "react";
import axios from 'axios';
import './Auth.css';

const Auth = () => {
    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const header = { 'Authorization': 'Basic YnVnaW5hdG9yV2ViQXBwOnNlY3JldA==', 'Content-Type': 'application/x-www-form-urlencoded' };
    


    const isValid = () => {
        return userName.length > 0 && password.length > 0;
    }

    const submitHandler = e => {
        e.preventDefault();

        const formBody = [];
        formBody.push('grant_type=password')
        formBody.push('username=' + userName)
        formBody.push('password=' + password)
        formBody.push('clientId=buginatorWebApp')
        formBody.push('clientSecret=$2a$10$yQKiHrX2tKiyDo7WODXk6OkpdVcpAXFTLPG62hlCdbL2qEQ62uqZC')
             
        const url = 'http://localhost:8100/oauth/token';

        const optionsAxios = {
            url,
            method: 'POST',
            headers: header,
            data: formBody.join('&')
        }

          axios(optionsAxios)
            .then(res =>  localStorage.setItem('token', res.data.access_token)
            )
            .then(res => console.log('Przekierowanie do DASZbordu'));

    }




    return (
        <div className="Login">
            <form onSubmit={submitHandler}>
                <label>Email: </label>
                <input type="email" onChange={e => setUserName(e.target.value)} autoFocus={true}/>
               
                <label>Password: </label>
                <input type="password" onChange={e => setPassword(e.target.value)} />

               
                <button disabled={!isValid()}>Login</button>
            </form>
        </div>
    );
}

export default Auth;

// private clientId: string = "buginatorWebApp";
//   private clientSecret: string = "$2a$10$yQKiHrX2tKiyDo7WODXk6OkpdVcpAXFTLPG62hlCdbL2qEQ62uqZC";
//   private clientBtoa: string = "YnVnaW5hdG9yV2ViQXBwOnNlY3JldA==";