import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import CreatePersonnage from "./createCharater.tsx";
import Game from "./Game";

const App: React.FC = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<CreatePersonnage />} />
                <Route path="/game" element={<Game />} />
            </Routes>
        </Router>
    );
};

export default App;
