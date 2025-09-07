#ifndef LAB_6_7_EXCEPTII_H
#define LAB_6_7_EXCEPTII_H

#include <string>
#include <exception>

class RepoException : public std::exception {
public:
    /**
     * Constructorul clasei RepoException
     * @param m Mesajul de eroare
     */
    explicit RepoException(const std::string m) : msg(std::move(m)) {}

    /**
     * Suprascrierea metodei what() din clasa std::exception
     * @return Mesajul de eroare
     */
    const char *what() const noexcept override {
        return msg.c_str();
    }

private:
    std::string msg;
};

class ValidationException : public std::exception {
public:
    /**
     * Constructorul clasei ValidationException
     * @param m Mesajul de eroare
     */
    explicit ValidationException(const std::string m) : msg(std::move(m)) {}

    /**
     * Suprascrierea metodei what() din clasa std::exception
     * @return Mesajul de eroare
     */
    const char *what() const noexcept override {
        return msg.c_str();
    }

private:
    std::string msg;
};

#endif //LAB_6_7_EXCEPTII_H
