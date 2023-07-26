import { useContext } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import { BasicAuthContext } from "../contexts/BasicAuthContext";
import axios from "axios";
import BasicAuth from "../promises/BasicAuth";
import { useCookies } from "react-cookie";

function RootRoute() {
  const authContext = useContext(BasicAuthContext);
  const [cookies, removeCookie] = useCookies("JSESSIONID");
  const navigate = useNavigate();

  const handlerLogin = () => {
    navigate("/login", { replace: true });
  };

  //To investigate more - why if use post method not able to set the cookie 
  //although the relevant reponse returns the 'Set-Cookie'
  const handlerLogout = async () => {
    let response = null;
    try {
      response = await BasicAuth.get("/nutrimate/logout", {
        withCredentials: true,
        headers: {
          Accept: "application/json",
        },
      });

      // removeCookie("JSESSIONID", "", {maxAge: 0, path: "/", secure: true});
      authContext.setLogoutStatus(true);
      authContext.setLoginStatus(false);
      authContext.resetDisplayName();
      navigate("/login", { replace: true });

      console.log("Logout - GET status", response);
      console.log("Logout - GET status", response.status);
    } catch (error) {
      authContext.setLogoutStatus(false);
      authContext.setLoginStatus(true);
      console.log("POST Logout error", error);
    }
  };

  const handlerProfile = async () => {
    let response = null;
    try {
      response = await BasicAuth.get("/nutrimate/customers/profile", {
        withCredentials: true,
        headers: {
          Accept: "application/json",
        },
      });

      // navigate("/login", { replace: true });
      console.log("Profile - GET response", response);
      console.log("Profile - GET status", response.data);
    } catch (error) {
      console.log("GET Profile error", error.response.data);
    }
  };

  return (
    <div className="row w-100 p-4 d-flex justify-content-end">
      <div className="col-md-4 d-flex justify-content-center">
        <p className="fs-1 fw-bold text-dark pt-2 mb-0 pb-2">
          Welcome to Nutrimate
        </p>
      </div>
      <div className="col-md-1 d-flex align-items-center justify-content-center">
        {authContext.isLoggedIn ? (
          <input
            className="btn btn-primary btn-sm btn-block"
            type="submit"
            value="Logout"
            onClick={handlerLogout}
          />
        ) : (
          <input
            className="btn btn-primary btn-sm btn-block"
            type="submit"
            value="Login"
            onClick={handlerLogin}
          />
        )}
      </div>
      <div className="col-md-1 d-flex align-items-center justify-content-center">
        {authContext.isLoggedIn && (
          <input
            className="btn btn-primary btn-sm btn-block"
            type="submit"
            value="profile"
            onClick={handlerProfile}
          />
        )}
      </div>
      <div className="col-md-2 d-flex align-items-center justify-content-center">
        <p className="text-center fs-6 mb-2">{`Hello, ${authContext.displayName}`}</p>
      </div>
      <Outlet />
    </div>
  );
}

export default RootRoute;
