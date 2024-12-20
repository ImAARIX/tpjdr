import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import * as CharacterApi from "./api/CharacterApi";
import CreationPersonnage, { ClassTypeEnum } from "./createCharater";

const mockNavigate = jest.fn();

jest.mock("./api/CharacterApi", () => ({
  registerCharacter: jest.fn(),
}));

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
  MemoryRouter: ({ children }: { children: React.ReactNode }) => <div>{children}</div>
}));

describe("CreationPersonnage Component", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  afterEach(() => {
    jest.resetAllMocks();
  });

  const renderComponent = () => {
    return render(
        <MemoryRouter>
          <CreationPersonnage />
        </MemoryRouter>
    );
  };

  test("renders the component correctly", () => {
    // Arrange
    renderComponent();

    // Act
    const resultPersonnage = screen.queryByText(/Création de Personnage/i);
    const resultNom = screen.queryByPlaceholderText(/Entrez le nom/i);
    const resultClasse = screen.queryByText(/Choisissez une classe/i);
    const resultBouton = screen.queryByText(/Créer le personnage/i);

    // Assert
    expect(resultPersonnage).not.toBeNull();
    expect(resultNom).not.toBeNull();
    expect(resultClasse).not.toBeNull();
    expect(resultBouton).not.toBeNull();
  });

  test("updates name field on user input", () => {
    // Arrange
    renderComponent();
    const nameInput = screen.queryByPlaceholderText(/Entrez le nom/i);

    // Act
    if (nameInput) {
      fireEvent.change(nameInput, { target: { value: "TestName" } });
    }

    // Assert
    if (nameInput) {
      expect((nameInput as HTMLInputElement).value).toBe("TestName");
    }
  });

  test("updates class and displays characteristics on selection", () => {
    // Arrange
    renderComponent();
    const classSelect = screen.getByRole('combobox');

    // Act
    fireEvent.change(classSelect, { target: { value: ClassTypeEnum.WARRIOR } });

    // Assert
    expect(screen.queryByText(/Strength : 10/i)).not.toBeNull();
    expect(screen.queryByText(/Intelligence : 5/i)).not.toBeNull();
    expect(screen.queryByText(/Luck : 3/i)).not.toBeNull();
  });

  test("shows an alert if name or class is missing on submit", () => {
    // Arrange
    window.alert = jest.fn();
    renderComponent();
    const submitButton = screen.queryByText(/Créer le personnage/i);

    // Act
    if (submitButton) {
      fireEvent.click(submitButton);
    }

    // Assert
    expect(window.alert).toHaveBeenCalledWith("Veuillez entrer un nom et choisir une classe !");
  });

  test("submits data and navigates on successful API call", async () => {
    // Arrange
    (CharacterApi.registerCharacter as jest.Mock).mockResolvedValue({
      id: 1,
      username: "TestName",
      classType: ClassTypeEnum.WARRIOR
    });
    renderComponent();
    const nameInput = screen.getByPlaceholderText(/Entrez le nom/i);
    const classSelect = screen.getByRole('combobox');
    const submitButton = screen.getByRole('button', { name: /Créer le personnage/i });

    // Act
    fireEvent.change(nameInput, { target: { value: "TestName" } });
    fireEvent.change(classSelect, { target: { value: ClassTypeEnum.WARRIOR } });
    fireEvent.click(submitButton);

    // Assert
    await waitFor(() => {
      expect(CharacterApi.registerCharacter).toHaveBeenCalledWith({
        username: "TestName",
        classType: ClassTypeEnum.WARRIOR,
      });
      expect(window.alert).toHaveBeenCalledWith("Personnage créé avec succès !");
      expect(mockNavigate).toHaveBeenCalledWith('/game', {
        state: { personnage: expect.any(Object) }
      });
    });
  });

  test("shows an error alert on API failure", async () => {
    // Arrange
    (CharacterApi.registerCharacter as jest.Mock).mockRejectedValue(new Error("API error"));
    window.alert = jest.fn();
    renderComponent();
    const nameInput = screen.getByPlaceholderText(/Entrez le nom/i);
    const classSelect = screen.getByRole('combobox');
    const submitButton = screen.getByRole('button', { name: /Créer le personnage/i });

    // Act
    fireEvent.change(nameInput, { target: { value: "TestName" } });
    fireEvent.change(classSelect, { target: { value: ClassTypeEnum.MAGE } });
    fireEvent.click(submitButton);

    // Assert
    await waitFor(() => {
      expect(CharacterApi.registerCharacter).toHaveBeenCalledWith({
        username: "TestName",
        classType: ClassTypeEnum.MAGE,
      });
      expect(window.alert).toHaveBeenCalledWith("Une erreur est survenue. Veuillez réessayer.");
    });
  });
});
