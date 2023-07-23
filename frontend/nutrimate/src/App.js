import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import "./App.css";
import Login from "./routes/Login";
import RootRoute from "./routes/RootRoute";
import Home from "./routes/Home";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<RootRoute />}>
          <Route index element={<Home />} />
          <Route path="/home" element={<Home />} />
          <Route path="/login" element={<Login />} />
          {/* {authCtx.isLoggedIn && <Route path="/game" element={<Game />} />}
          <Route path="/about" element={<About />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/login" element={<Login />} />
          <Route path="/logout" element={<Logout />} />
          <Route path="*" element={<Navigate to="/login" replace={true} />} /> */}
          {/**
           *
           * Use navigate here at the catch all route will load the path to the new route
           * e.g. if you type https://rooturl/game and is invalid, if we do not use the Navigate component,
           * the path will remain as https://rooturl/game instead of re-routed to https://rooturl/login
           *
           */}
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
