import React from "react";

const Header = () => {

    return (
        <div>
            <header>
                <nav className="navbar navbar-dark bg-dark">

                    <div className="container">
                        <div><a href="http://localhost:3000/"><img src="image/salon.png" /></a></div>
                        <a className="navbar-brand mr-auto" href="http://localhost:3000/">Home</a>
                        <a className="navbar-brand" href="http://localhost:3000/list">등록</a>
                        <a className="navbar-brand" href="http://localhost:3000/road">조회</a>
                        <a className="navbar-brand" href="http://localhost:3000/search">굿</a>
                    </div>

                </nav>
            </header>
        </div>
    )
}

export default Header;