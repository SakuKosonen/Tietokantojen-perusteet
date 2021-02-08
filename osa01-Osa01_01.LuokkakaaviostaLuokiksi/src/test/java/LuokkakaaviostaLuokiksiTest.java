
import org.junit.Test;
import org.junit.Rule;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Points("01-01")
public class LuokkakaaviostaLuokiksiTest {

    @Rule
    public MockStdio io = new MockStdio();

    @Test
    public void luokkaKirja() {
        ensureClassAndAttribute("Kirja", "nimi", String.class);
        ensureClassAndAttribute("Kirja", "kirjoittaja", String.class);
        ensureClassAndAttribute("Kirja", "julkaisuvuosi", Integer.class);
    }

    @Test
    public void luokkaNide() {
        ensureClassAndAttribute("Nide", "tunnus", Integer.class);
    }

    @Test
    public void luokkaLaina() {
        ensureClassAndAttribute("Laina", "alku", LocalDate.class);
        ensureClassAndAttribute("Laina", "loppu", LocalDate.class);
        ensureClassAndAttribute("Laina", "palautettu", Boolean.class);
    }

    @Test
    public void luokkaHylly() {
        ensureClassAndAttribute("Hylly", "sijainti", String.class);
    }

    @Test
    public void luokkaHenkilo() {
        ensureClass("Henkilo");
    }

    @Test
    public void viiteKirjastaNiteeseen() {
        ensureClassAndAttributeWithUnknownName("Kirja", List.class, Reflex.reflect("Nide").cls());
    }

    @Test
    public void viiteHyllystaNiteeseen() {
        ensureClassAndAttributeWithUnknownName("Hylly", List.class, Reflex.reflect("Nide").cls());
    }

    @Test
    public void viiteHenkilostaLainaan() {
        ensureClassAndAttributeWithUnknownName("Henkilo", List.class, Reflex.reflect("Laina").cls());
    }
    
    @Test
    public void viiteNiteestaLainaan() {
        ensureClassAndAttributeWithUnknownName("Nide", List.class, Reflex.reflect("Laina").cls());
    }

    @Test
    public void eiTurhiaAttribuuttejaKirja() {
        limitAttributeCount("Kirja", 4);
    }

    @Test
    public void eiTurhiaAttribuuttejaHylly() {
        limitAttributeCount("Hylly", 2);
    }

    @Test
    public void eiTurhiaAttribuuttejaLaina() {
        limitAttributeCount("Laina", 5);
    }

    private Class ensureClass(String className) {
        Class clazz = Reflex.reflect(className).cls();
        if (clazz == null) {
            fail("Unable to find class " + className);
        }
        return clazz;
    }

    private void ensureClassAndAttribute(String className, String attributeName, Class type) {
        Class clazz = ensureClass(className);

        Class actualType = null;
        try {
            actualType = clazz.getDeclaredField(attributeName).getType();
        } catch (NoSuchFieldException ex) {
            fail("Class " + className + " does not have the attribute " + attributeName + ".");
        } catch (SecurityException ex) {
            Logger.getLogger(LuokkakaaviostaLuokiksiTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        assertEquals("Expected that the class " + className + " attribute was of type " + type + ". Now type was " + actualType + ".", type, actualType);
    }

    private void ensureClassAndAttributeWithUnknownName(String className, Class type, Class collectionType) {
        Class clazz = ensureClass(className);

        Field actualField = null;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(type)) {
                actualField = field;
            }
        }

        assertTrue("Unable to find an attribute with type " + type + " from class " + className + ".", actualField != null);

        if (collectionType != null) {
            ParameterizedType listType = (ParameterizedType) actualField.getGenericType();
            Class<?> actualCollectionType = (Class<?>) listType.getActualTypeArguments()[0];

            assertEquals("Expected collection type " + collectionType + " but the type was " + actualCollectionType + " for the class " + className + ".", collectionType, actualCollectionType);
        }

    }

    private void limitAttributeCount(String className, int maxAttributes) {
        Class clazz = ensureClass(className);
        int declaredFieldCount = clazz.getDeclaredFields().length;

        assertTrue(declaredFieldCount <= maxAttributes);
    }
}
