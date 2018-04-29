package org.flysky.coder.vo.user;

public class AnonymousNameGenerator {
    private static String []names={"王A","王B","王C","王D","王E","王F","王G","王H","王I","王J","王K","王M","王N","王O","王P","王Q","王R","王S","王T","王U","王V","王W","王X","王Y","王Z",
            "李A","李B","李C","李D","李E","李F","李G","李H","李I","李J","李K","李M","李N","李O","李P","李Q","李R","李S","李T","李U","李V","李W","李X","李Y","李Z"};
    public static String autoGenerate(){
        return names[(int)Math.random()*names.length];
    }
}
