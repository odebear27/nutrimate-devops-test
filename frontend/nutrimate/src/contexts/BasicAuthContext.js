import { createContext, useState } from "react";

export const BasicAuthContext = createContext({
  credentials: {},
  handlerUserNameChange: () => {},
  handlerPasscodeChange: () => {},
  isShowAuthError: false,
  setShowAuthError: () => {},
  authErrorMessage: "",
  setAuthErrorMessage: () => {},
  isLoggedIn: false,
  setLoginStatus: () => {},
  isLoggedOut: false,
  setLogoutStatus: () => {},
  resetCredentials: () => {},
  setDisplayName: () => {},
  displayName: "",
  resetDisplayName: () => {}
});

const initCredentials = {
  username: "",
  passcode: "",
};

const initDisplayName = "Anonymous"

export function BasicAuthContextProvider({ children }) {
  const [credentials, setCredentials] = useState(initCredentials);
  const [displayName, setDisplayName] = useState(initDisplayName);
  const [isShowAuthError, setShowAuthError] = useState(false);
  const [authErrorMessage, setAuthErrorMessage] = useState("");
  const [isLoggedIn, setLoginStatus] = useState(false);
  const [isLoggedOut, setLogoutStatus] = useState(false);

  const handlerUserNameChange = (value) => {
    setCredentials({
      ...credentials,
      username: value,
    });
  };

  const handlerPasscodeChange = (value) => {
    setCredentials({
      ...credentials,
      passcode: value,
    });
  };

  const resetCredentials = () => {
    setCredentials(initCredentials);
  }

  const resetDisplayName = () => {
    setDisplayName(initDisplayName);
  }

  const authContext = {
    credentials: credentials,
    handlerUserNameChange: handlerUserNameChange,
    handlerPasscodeChange: handlerPasscodeChange,
    isShowAuthError: isShowAuthError,
    setShowAuthError: setShowAuthError,
    authErrorMessage: authErrorMessage,
    setAuthErrorMessage: setAuthErrorMessage,
    isLoggedIn: isLoggedIn,
    setLoginStatus: setLoginStatus,
    isLoggedOut: isLoggedOut,
    setLogoutStatus: setLogoutStatus,
    resetCredentials: resetCredentials,
    setDisplayName: setDisplayName,
    displayName: displayName,
    resetDisplayName: resetDisplayName
  };

  return (
    <BasicAuthContext.Provider value={authContext}>
      {children}
    </BasicAuthContext.Provider>
  );
}
