function Signin({onClick}) {
  return (
    <div className="d-grid gap-2">
      <input
        className="btn btn-primary btn-block"
        type="submit"
        value="Sign in"
        onClick={onClick}
      />
    </div>
  );
}

export default Signin;