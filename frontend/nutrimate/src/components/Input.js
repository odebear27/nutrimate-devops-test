function Input({divClassName, labelName, value, type, className, id, name, placeholder, onChange}){

    const handlerChange = (event) => {
        const value = event.target.value;
        onChange(value);
    }

    return (
        <div className={divClassName}>
            <label htmlFor={name} className="form-label">
              {labelName}
            </label>
            <input
              value={value}
              type={type}
              className={className}
              id={id}
              name={name}
              placeholder={placeholder}
              onChange={handlerChange}
            />
        </div>
    );
}

export default Input;