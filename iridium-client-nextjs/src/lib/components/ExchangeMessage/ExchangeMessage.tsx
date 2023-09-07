'use client'

import React, { useEffect, useState } from 'react';

export default function ExchangeMessage() {
    const [isSuccessful, setIsSuccessful ]  = useState(false);

    function ExchangeMessage({success}) {
        if (success) {
            return <div>You have successfully authorized this application with Iridium</div>
        }
        return <div>There was an error during authorization</div>

    }

    return (
        <ExchangeMessage success={isSuccessful}/>

    )

}
